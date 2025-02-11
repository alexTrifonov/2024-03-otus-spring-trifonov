<script>
    export default {
        props: {
            itemData: {
                type: Array
            },
            fields: {
                type: Array
            }
        },
        data() {
            return {
                nFields: this.fields,
                items: this.itemData
            }
        },
        mounted() {
            this.nFields.push('Comments','Updating', 'Deleting')
        }
            
    }
</script>


<template>
    <table class="table">
        <thead>
            <tr>
                <th v-for="field in nFields" :key="field">
                    {{ field }}
                </th>
            </tr>
        </thead>
        <tbody>
            <tr v-for="item in items" :key="item">
                <td v-for="field in nFields" :key="field">
                    <div v-if="field === 'Comments'">
                        <RouterLink :to="`books/${item.Id}/comments`">Comments</RouterLink>
                    </div>
                    <div v-else>{{ item[field] }}</div>
                    <div v-if="field === 'Updating'">
                        <RouterLink :to="`books/${item.Id}/updating`">Update</RouterLink>
                    </div>
                    <div v-if="field === 'Deleting'">
                        <RouterLink :to="`books/${item.Id}/deleting`">Delete</RouterLink>
                    </div>
                    
                </td>
            </tr>
        </tbody>
    </table>
</template>

<style scoped>
    .table {
            border: 1px solid steelblue;
            width: 300px;
            border-collapse: collapse;
        }

        .table tr td, th {
            padding: 5px;
            border: 1px solid steelblue;
        }

        .table td:last-child, td:first-child {
            width: 50px;
        }
</style>